package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.AuthenticationService;
import com.social.socialnetwork.Service.twilio.TwillioService;
import com.social.socialnetwork.config.JwtService;
import com.social.socialnetwork.dto.*;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.Role;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.ConfirmationCodeRepository;
import com.social.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceIplm implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final ConfirmationCodeRepository confirmationCodeRepository;
  private final TwillioService twillioService;

  @Override
  public ConfirmationCode GetNewOTP(String uid) {
    ConfirmationCode verificationToken = confirmationCodeRepository.findVerificationTokenByUserId(
        uid);
    String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    verificationToken.setToken(token);
    confirmationCodeRepository.save(verificationToken);
    return verificationToken;
  }

  @Override
  public AuthenticationResponse validateOTP(PhoneVerifyReq phoneVerifyReq) {
    // Find user
    User verifyingUser = userRepository.findById(phoneVerifyReq.getUserID()).get();
    // Get user's phone number from database
    String verifyingPhoneNumber = verifyingUser.getPhone();
    // Get Token requested
    String code = phoneVerifyReq.getCode();

    if (verifyPhoneNumber(verifyingPhoneNumber)) {

      String formattedPhoneNumber = formatPhoneNumber(verifyingPhoneNumber);
      ConfirmationCode verificationToken
          = confirmationCodeRepository.findConfirmationCodeByCodeAndUserPhone(code,
          formattedPhoneNumber);

      if (verificationToken == null) {
        return null;
      }

      User user = verificationToken.getUser();
      Calendar cal = Calendar.getInstance();

      if ((verificationToken.getExpirationTime().getTime()
          - cal.getTime().getTime()) <= 0) {
        confirmationCodeRepository.delete(verificationToken);
        return null;
      }
      verificationToken.setToken(null);
      confirmationCodeRepository.save(verificationToken);
      user.setEnabled(true);
      user.setRole(Role.USER);
      userRepository.save(user);
      var jwtToken = jwtService.generateToken(user);
      return AuthenticationResponse.builder()
          .token(jwtToken).build();
    } else {
      return null;
    }

  }

  public boolean verifyPhoneNumber(String phone) {
    // Get Utility Instance
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
    try {
      numberProto = phoneNumberUtil.parse(phone, "VN");
    } catch (NumberParseException e) {
      System.err.println("NumberParseException was thrown: " + e);
    }
    return phoneNumberUtil.isValidNumber(numberProto);
  }

  @Override
  public String formatPhoneNumber(String phone) {
    // Get Utility Instance
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
    try {
      numberProto = phoneNumberUtil.parse(phone, "VN");
    } catch (NumberParseException e) {
      System.err.println("NumberParseException was thrown: " + e);
    }
    String formattedNum = phoneNumberUtil.format(numberProto,
        PhoneNumberUtil.PhoneNumberFormat.E164);
    return formattedNum;
  }

  @Override
  public User register(RegisterReqest request) {

    if (!GenericValidator.isEmail(request.getEmail())) {
      throw new AppException(400, "Wrong email");
    }
    boolean check = userRepository.existsByEmail(request.getEmail());
    if (check) {
      throw new AppException(400, "Email already exits");
    }

    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .Enabled(request.getEnabled())
        .build();
    return userRepository.save(user);
  }

  @Override
  public User saveRegister(UserReq userReq, String prefix) {

    boolean check = userRepository.existsByPhone(userReq.getPhone());
    if (check) {
      throw new AppException(400, "Phone already used by orther account");
    }
    User user = User.builder()
        .firstName(userReq.getFirstName())
        .lastName(userReq.getLastName())
        .email(userReq.getEmail())
        .phone(userReq.getPhone())
        .password(passwordEncoder.encode(userReq.getPassword()))
        .role(Role.USER)
        .Enabled(userReq.getEnabled())
        .build();
    user.setRole(Role.USER);
    user.setAddress(userReq.getAddress());

    return userRepository.save(user);
  }

  @Override
  public String resetPassReqByPhone(UserReq userReq, String prefix) {
    // Get Utility Instance
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    // +84 + 091913123213
    String phoneNo = userReq.getPhone();
    phoneNo = phoneNo.substring(1);
    String originalNum = prefix + phoneNo;
    // if phone number is valid
    if (userRepository.existsByPhone(originalNum)) {
      if (phoneNumberUtil.isPossibleNumber(originalNum, prefix)) {
        //Format the number
        Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
        try {
          numberProto = phoneNumberUtil.parse(originalNum, "VN");
        } catch (NumberParseException e) {
          System.err.println("NumberParseException was thrown: " + e);
        }

        String formattedNum = phoneNumberUtil.format(numberProto,
            PhoneNumberUtil.PhoneNumberFormat.E164);
        User user = userRepository.findByPhone(formattedNum);
        //Send Token
        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        saveVerificationCodeForUser(user, token);
        twillioService.sendSMS(formattedNum, token);

        return formattedNum;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public String registerByPhone(UserReq userReq, String prefix) {
    // Get Utility Instance
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    // +84 + 091913123213
    String phoneNo = userReq.getPhone();
    phoneNo = phoneNo.substring(1);
    String originalNum = prefix + phoneNo;
    // if phone number is valid
    if (!userRepository.existsByPhone(userReq.getPhone())) {
      if (phoneNumberUtil.isPossibleNumber(originalNum, prefix)) {
        //Format the number
        Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
        try {
          numberProto = phoneNumberUtil.parse(originalNum, "VN");
        } catch (NumberParseException e) {
          System.err.println("NumberParseException was thrown: " + e);
        }

        String formattedNum = phoneNumberUtil.format(numberProto,
            PhoneNumberUtil.PhoneNumberFormat.E164);
        // Save the user
        userReq.setPhone(formattedNum);

        User user = saveRegister(userReq, prefix);
        //Send Token
        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        saveVerificationCodeForUser(user, token);
        twillioService.sendSMS(formattedNum, token);

        return formattedNum;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public AuthenticationResponse authenticatebyPhone(PhoneLoginRequest reqest) {
    String loginPhoneNumber = formatPhoneNumber(reqest.getPhone());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginPhoneNumber,
            reqest.getPassword()
        )
    );
    var user = userRepository.findUserByPhone(loginPhoneNumber)
        .orElseThrow();
    if (!user.getEnabled()) {
      throw new AppException(400, "User not authenticate");
    } else {
      var jwtToken = jwtService.generateToken(user);
      return AuthenticationResponse.builder()
          .token(jwtToken).build();
    }
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationReqest reqest) {
    User user = null;
    if (reqest.getPhone() != null || reqest.getEmail() != null) {
      if (reqest.getEmail() != null) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                reqest.getEmail(),
                reqest.getPassword()
            )
        );
        user = userRepository.findByEmail(reqest.getEmail())
            .orElseThrow();
      } else if (reqest.getPhone() != null) {
        String loginPhoneNumber = formatPhoneNumber(reqest.getPhone());
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginPhoneNumber,
                reqest.getPassword()
            )
        );
        user = userRepository.findUserByPhone(loginPhoneNumber)
            .orElseThrow();
      }

      if (!user.getEnabled()) {
        throw new AppException(400, "User not authenticate");
      } else {
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken).build();
      }
    } else {
      return null;
    }

  }

  @Override
  public void saveVerificationCodeForUser(User users, String token) {
    ConfirmationCode verificationToken = new ConfirmationCode(users, token);
    System.out.println(verificationToken.getExpirationTime());
    confirmationCodeRepository.save(verificationToken);
  }

  @Override
  public AuthenticationResponse validateVerificationCode(String code, String email) {
    User userVeri = userRepository.findUserByEmail(email);
    ConfirmationCode verificationCode
        = confirmationCodeRepository.findVerificationCodeByCodeAndUserEmail(code, email);

    if (verificationCode == null) {
      throw new AppException(400, "User not validated");
    }

    verificationCode.setToken(null);
    confirmationCodeRepository.save(verificationCode);
    userVeri.setEnabled(true);
    userVeri.setRole(Role.USER);
    userRepository.save(userVeri);

    var jwtToken = jwtService.generateToken(userVeri);
    return AuthenticationResponse.builder()
        .token(jwtToken).build();

  }

  @Override
  public User validateVerificationCodetoResetPassword(PasswordDTO passwordDTO) {

    ConfirmationCode verificationCode
        = confirmationCodeRepository.findVerificationCodeByCodeAndUserEmail(
        passwordDTO.getVerifyCode(), passwordDTO.getEmail());
    if (verificationCode == null) {
      verificationCode
          = confirmationCodeRepository.findConfirmationCodeByCodeAndUserPhone(
          passwordDTO.getVerifyCode(), passwordDTO.getPhone());
      if (verificationCode == null) {
        throw new AppException(400, "User not validated");
      }
    }

    User user = userRepository.findUserByEmail(passwordDTO.getEmail());
    if (user == null) {
      user = userRepository.findByPhone(passwordDTO.getPhone());
    }
    verificationCode.setToken(null);
    confirmationCodeRepository.save(verificationCode);
    user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
    userRepository.save(user);
    return user;
  }
}
