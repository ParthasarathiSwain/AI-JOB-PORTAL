package com.otz.service;

import com.otz.entity.ParsedResume;
import com.otz.entity.Resume;
import com.otz.repository.ParsedResumeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ResumeParsingService {

    private final ParsedResumeRepository parsedResumeRepository;

    public ParsedResume parseAndSaveResume(Resume resume) throws IOException {
        String filePath = resume.getFilePath();
        String text = extractText(filePath);

        List<String> skills = extractSkills(text);
        String experience = extractExperience(text);
        String education = extractEducation(text);

        ParsedResume parsed = ParsedResume.builder()
                .resume(resume)
                .extractedSkills(String.join(",", skills))
                .extractedExperience(experience)
                .extractedEducation(education)
                .build();

        ParsedResume saved = parsedResumeRepository.save(parsed);

        // Link back to Resume
        resume.setParsedResume(saved);
        return saved;
    }


    // Extract text from PDF or DOCX
    private String extractText(String filePath) throws IOException {
        if (filePath.endsWith(".pdf")) {
            try (PDDocument doc = PDDocument.load(new File(filePath))) {
                return new PDFTextStripper().getText(doc);
            }
        } else if (filePath.endsWith(".docx")) {
            try (FileInputStream fis = new FileInputStream(filePath);
                 XWPFDocument document = new XWPFDocument(fis)) {
                StringBuilder sb = new StringBuilder();
                for (XWPFParagraph para : document.getParagraphs()) {
                    sb.append(para.getText()).append("\n");
                }
                return sb.toString();
            }
        }
        throw new RuntimeException("Unsupported file format");
    }

    // Extract skills using regex or keyword list
    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        String[] commonSkills = {"Java", "Python", "Spring", "Hibernate", "React", "Angular", "SQL", "AWS", "Docker", "Git"};
        for (String skill : commonSkills) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(skill) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) skills.add(skill);
        }
        return skills;
    }

    // Extract experience (years)
    private String extractExperience(String text) {
        Pattern pattern = Pattern.compile("(\\d+)\\s+years?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        List<String> expList = new ArrayList<>();
        while (matcher.find()) expList.add(matcher.group());
        return String.join(", ", expList);
    }

    // Extract education keywords
    private String extractEducation(String text) {
        String[] eduKeywords = {"B.Tech", "M.Tech", "B.Sc", "M.Sc", "MBA", "BE", "ME"};
        List<String> eduList = new ArrayList<>();
        for (String edu : eduKeywords) {
            if (text.toLowerCase().contains(edu.toLowerCase())) eduList.add(edu);
        }
        return String.join(", ", eduList);
    }
}
