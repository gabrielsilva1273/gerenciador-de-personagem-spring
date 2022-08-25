package com.gerenciadordepersonagem.security.service;

import com.gerenciadordepersonagem.email.EmailSender;
import com.gerenciadordepersonagem.email.EmailValidator;
import com.gerenciadordepersonagem.security.model.RegistrationRequest;
import com.gerenciadordepersonagem.security.model.RegistrationToken;
import com.gerenciadordepersonagem.security.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {
    private UserService userService;
    private EmailValidator emailValidator;
    private RegistrationTokenService registrationTokenService;
    private EmailSender emailSender;

    public String register (RegistrationRequest request) {
        String userName = request.getUserName().trim();
        int maxUserNameSize = 20;
        boolean isUserNameNotValid =
                (userName.isBlank() || userName.isEmpty() || userName.length() > maxUserNameSize);
        if (isUserNameNotValid) {
            throw new IllegalStateException("Nome inválido");
        }

        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email inválido");
        }

        boolean isPasswordValid = (request.getPassword().length() >= 14 && request.getPassword().length() <= 1000);
        if (!isPasswordValid) {
            throw new IllegalStateException("Tamanho de senha inválido. (min=14,max=1000)");
        }

        boolean isConfirmationPasswordValid = request.getPassword().equals(request.getConfirmPassword());
        if (!isConfirmationPasswordValid){
            throw new IllegalStateException("Senhas diferentes");
        }

        String token = userService.signUpUser(
                new User(request.getUserName().trim(), request.getEmail().trim(), request.getPassword()));

        String link = "https://gerenciador-de-personagem.herokuapp.com/auth/confirmar?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getUserName(), link));

        return "Cadastro realizado com sucesso, acesse " + request.getEmail() + " para finalizar o seu cadastro.";
    }

    @Transactional
    public String confirmToken (String token) {
        Optional <RegistrationToken> registrationTokenOptional = registrationTokenService.getToken(token);
        boolean isRegistrationTokenOptionalEmpty = registrationTokenOptional.isEmpty();
        if (isRegistrationTokenOptionalEmpty) {
            throw new RuntimeException("Token inexistente");
        }

        RegistrationToken registrationToken = registrationTokenOptional.get();

        if (registrationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email já confirmado");
        }

        LocalDateTime expiredAt = registrationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }

        registrationTokenService.setConfirmedAt(token);
        userService.enableUser(registrationToken.getUser().getEmail());
        return "Token confirmado com sucesso.";
    }
    private String buildEmail (String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n" + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n" + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" + "    <tbody><tr>\n" + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n" + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" + "          <tbody><tr>\n" + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n" + "                    <td style=\"padding-left:10px\">\n" + "                  \n" + "                    </td>\n" + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirme seu e-mail</span>\n" + "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n" + "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n" + "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n" + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" + "      <td>\n" + "        \n" + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n" + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" + "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n" + "  </tbody></table>\n" + "\n" + "\n" + "\n" + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "    <tr>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" + "        \n" + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Obrigado por se registrar. Por favor clique no link abaixo para ativar sua conta: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Ativar</a> </p></blockquote>\n Esse link irá expirar em 20 minutos. <p>Vejo você em breve!</p>" + "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "    </tr>\n" + "    <tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>";
    }
}
