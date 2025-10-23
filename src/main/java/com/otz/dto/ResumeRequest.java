package com.otz.dto;

import com.otz.entity.ResumePrivacy;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResumeRequest {
    private MultipartFile file;
    private ResumePrivacy privacy;
}
