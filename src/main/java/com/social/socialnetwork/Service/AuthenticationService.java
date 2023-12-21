package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.*;

import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.User;

public interface AuthenticationService {
     ConfirmationCode GetNewOTP(String uid);

     AuthenticationResponse validateOTP(verifycationDTO verifyDTO);

     String formatPhoneNumber(String phone);

     User register(RegisterReqest request);
     AuthenticationResponse authenticate(AuthenticationReqest reqest);
     User validateVerificationCodetoResetPassword(PasswordDTO passwordDTO);
     void saveVerificationCodeForUser(User users, String token);
     AuthenticationResponse validateVerificationCode(verifycationDTO verifyDTO);

     User saveRegister(UserReq userReq, String prefix);

     String resetPassReqByPhone(UserReq userReq, String prefix);

     String registerByPhone(UserReq userReq, String prefix);

     AuthenticationResponse authenticatebyPhone(PhoneLoginRequest request);
}
