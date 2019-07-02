/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author Andy_Antunes
 */
public class EmailCreate {
    
    final private String mailSMTPServer;
    final private String mailSMTPServerPort;
    final private String user = "andersonlantunes@gmail.com";
    final private String pass = "lakers263766la";
    
    /*
     * Quando instanciar um Objeto já será atribuído o servidor SMPT do Gmail
     * e a porta usada por ele.
     */
    EmailCreate() { // Para o Gmail
        mailSMTPServer = "smtp.gmail.com";
        mailSMTPServerPort = "465";
    }
    
    /*
     * Caso queira mudar o servidor e a porta, só enviar para o construtor
     * os valores como String
     */
    EmailCreate(String mailSMTPServer, String mailSMTPServerPort) { // Para outro servidor
        this.mailSMTPServer = mailSMTPServer;
        this.mailSMTPServerPort = mailSMTPServerPort;        
    }
    
    public void sendMail(String from, String to, String subjetc, String message) {
        
        Properties props = new Properties();
        
        // Quem estiver utilizando um SERVIDOR PROXY descomente essa parte e
        // atribua as propriedades do SERVIDOR PROXY utilizado
        /*
        props.setProperty("proxySet","true");
        props.setProperty("socksProxyHost","192.168.155.1"); // IP do Servidor Proxy
        props.setProperty("socksProxyPort","1080");  // Porta do servidor Proxy
        */
        
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.enable", "true");
        
        props.put("mail.transport,protocol", "smtp"); // Define o protocolo de envio como SMTP
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailSMTPServer); // Server SMTP do Gmail
        props.put("mail.amtp.auth", "true"); // Ativa a autenticação
        props.put("mail.smtp.user", from); // Conta que está enviando o email (Gmail)
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", mailSMTPServerPort); // Porta
        props.put("mail.smtp.socketFactory.port", mailSMTPServerPort); // Mesma porta para o socket
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.sockerFactory.fallback", "false");
        
        // Cria um autenticador que será a seguir
        SimpleAuth auth;
        auth = new SimpleAuth(from, pass);
        
        // Session - Objeto que irá realizar a conexão com o servidor
        /*
         * Como há necessidade de autenticação é criada uma autenticação que
         * é responsável por solicitar e retornar o usuário e senha para
         * a autenticação.
        */
        
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true); // Habilita o LOG das ações durante o envio do email
        
        // Objeto que contém a mensagem
        Message msg = new MimeMessage(session);
        
        try {
            // Setando o destinatário
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Setando a origem do email
            msg.setFrom(new InternetAddress(from));
            // Setando o assunto
            msg.setSubject(subjetc);
            // Setando o conteúdo/corpo do email
            msg.setText(message);
            
        } catch (MessagingException e) {
            System.out.println(">> Erro: Completar Mensagem");
        }
            
            // Objeto encarregado de enviar os dados para o email
            Transport tr;
            try {
                tr = session.getTransport("smtp"); // Define smtp para tranporte
                /*
                 * 1 - Define o servidor smtp
                 * 2 - Seu nome de usuário do Gmail
                 * 3 - Sua senha do Gmail
                 */
                tr.connect(mailSMTPServer, from, pass);
                msg.saveChanges(); // don't forget this
                // Envio de mensagem
                tr.sendMessage(msg, msg.getAllRecipients());
                tr.close();
                
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                System.out.println(">> Erro: Envio de Mensagem");
            }
    }
    
    class SimpleAuth extends Authenticator {
        public String username = null;
        public String password = null;
        
        public SimpleAuth(String user, String pwd) {
            username = user;
            password = pwd;
        }
        
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pass);
        }
    }
}
