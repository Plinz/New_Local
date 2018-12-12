package com.newlocal.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.codec.Base64.OutputStream;
import com.newlocal.domain.Purchase;
import com.newlocal.domain.User;

import io.github.jhipster.config.JHipsterProperties;


@Component
public class PdfGeneratorUtil {
	
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private JHipsterProperties jHipsterProperties;
    
    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";
    
    private static final String PURCHASES = "purchases";
    
    public byte[] createPdf(String templateName, Context ctx) throws Exception {
        String processedHtml = templateEngine.process(templateName, ctx);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
        return os.toByteArray();
    }



	public byte[] createPdf(String templateName, User currentUser, List<Purchase> purchases) {
        Locale locale = Locale.forLanguageTag(currentUser.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, currentUser);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(PURCHASES, purchases);
        String processedHtml = templateEngine.process(templateName, context);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();

        } catch (DocumentException e) {
			e.printStackTrace();
		}
        finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
        return os.toByteArray();
	}
}