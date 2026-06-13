package com.ludens.assignment.post.presentation;

import com.ludens.assignment.global.annotation.LoginMember;
import com.ludens.assignment.post.application.PostApplicationService;
import com.ludens.assignment.post.presentation.dto.request.UpdatePostRequest;
import com.ludens.assignment.post.presentation.dto.response.CreatePostResponse;
import com.ludens.assignment.post.presentation.dto.response.PostResponse;
import com.ludens.assignment.post.presentation.dto.response.UpdatePostResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostApplicationService postApplicationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatePostResponse> createPost(
            @LoginMember String userId,
            @RequestPart @NotBlank String title,
            @RequestPart(required = false) String description,
            @RequestPart MultipartFile image
    ) {
        CreatePostResponse response = postApplicationService.createPost(userId, title, description, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, PostResponse>> getPost(
            @PathVariable Long id,
            @LoginMember(required = false) String loginUserId
    ) {
        PostResponse response = postApplicationService.getPost(loginUserId, id);
        return ResponseEntity.ok(Map.of("post", response));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Path imagePath = postApplicationService.getImagePath(id);
        if (imagePath == null || !imagePath.toFile().exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatePostResponse> updatePost(
            @PathVariable Long id,
            @LoginMember String userId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse response = postApplicationService.updatePost(
                userId, id, request.title(), request.description());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @LoginMember String userId
    ) {
        postApplicationService.deletePost(userId, id);
        return ResponseEntity.noContent().build();
    }
}
