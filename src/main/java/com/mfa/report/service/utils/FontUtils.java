package com.mfa.report.service.utils;

import com.itextpdf.text.Font;
import org.springframework.stereotype.Component;

@Component
public class FontUtils {
    public Font toMissionTitle(){
       return new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    }
}
