package com.appsv.loginapp.core.model

import java.util.Properties
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage

class MailSender(private val toEmail: String, private val subject: String, private val body: String) : Runnable {

    override fun run() {
        try {
            val properties = Properties().apply {
                put("mail.smtp.auth", "true")  // Enables authentication
                put("mail.smtp.ssl.enable", "true") // Enables SSL
                put("mail.smtp.host", "smtp.gmail.com") // Gmail SMTP server
                put("mail.smtp.port", "465") // SSL uses port 465
                put("mail.smtp.ssl.trust", "smtp.gmail.com") // Trusts Gmail server
            }

            val session = Session.getInstance(properties,
                object : jakarta.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication("madhavannapg@gmail.com", "vkvuqacfkpffjocm")
                    }
                })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("madhavannapg@gmail.com"))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                this.subject = subject
                setText(body)
            }

            Transport.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
