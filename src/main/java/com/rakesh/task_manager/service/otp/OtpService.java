package com.rakesh.task_manager.service.otp;

import com.rakesh.task_manager.dto.payload.OtpRequest;
import com.rakesh.task_manager.entity.Otp;
import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.exceptions.APIException;
import com.rakesh.task_manager.repo.OtpRepository;
import com.rakesh.task_manager.repo.UsersRepo;
import com.rakesh.task_manager.service.security.utils.OtpGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final OtpGenerator otpGenerator;
    private final UsersRepo usersRepo;
    public void sendOtp(String email){

        otpRepository.deleteByEmail(email);
        String otp= otpGenerator.otpGenerator();
        Otp saveOtp=new Otp();
        saveOtp.setOtp(otp);
        saveOtp.setEmail(email);
        saveOtp.setExpirationDate(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(saveOtp);
        emailService.sentOtp(otp,email);
    }
//    public void verifyOtp(OtpRequest request){
//        Users userByMail = usersRepo.findByEmail(request.getEmail()).orElseThrow(() ->
//                new APIException("User Not Found with that Mail...."));
//        Optional<Otp> otpByEmail = otpRepository.findByEmail(request.getEmail());
//        if(otpByEmail.isPresent()){
//            Otp otp1 = otpByEmail.get();
//            if(otp1.getExpirationDate().isBefore(LocalDateTime.now()))
//                throw new APIException("Otp is expired");
//            userByMail.setStatus("APPROVED");
//            usersRepo.save(userByMail);
//            otpRepository.deleteByEmail(request.getEmail());
//            return;
//        }
//        throw new APIException("OTP Not Found...Please Enter a valid otp");
//    }
    public void verifyOtp(OtpRequest request){
        Users user = usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new APIException("User not found with this email"));
        Otp otp = otpRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new APIException("Invalid Otp or Email"));
        if(otp.getExpirationDate().isBefore(LocalDateTime.now())){
            otpRepository.deleteByEmail(request.getEmail());
            throw new APIException("OTP expired");
        }
        if(!otp.getOtp().equals(request.getOtp())){
            throw new APIException("Invalid OTP");
        }
        user.setStatus("APPROVED");
        usersRepo.save(user);

        otpRepository.deleteByEmail(request.getEmail());
    }
}
