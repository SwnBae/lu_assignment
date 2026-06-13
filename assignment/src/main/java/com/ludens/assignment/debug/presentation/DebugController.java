package com.ludens.assignment.debug.presentation;

import com.ludens.assignment.debug.application.DebugApplicationService;
import com.ludens.assignment.debug.presentation.dto.DebugUserListItem;
import com.ludens.assignment.post.presentation.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final DebugApplicationService debugApplicationService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/allusers")
    public ResponseEntity<Map<String, List<DebugUserListItem>>> getAllUsers() {
        return ResponseEntity.ok(debugApplicationService.getAllUsers());
    }

    @GetMapping("/allposts")
    public ResponseEntity<Map<String, List<PostResponse>>> getAllPosts() {
        return ResponseEntity.ok(debugApplicationService.getAllPosts());
    }

    @GetMapping("/userraw/{username}")
    public ResponseEntity<Map<String, String>> getUserRaw(@PathVariable String username) {
        return ResponseEntity.ok(debugApplicationService.getUserRaw(username));
    }

    @GetMapping("/postraw/{postid}")
    public ResponseEntity<Map<String, String>> getPostRaw(@PathVariable Long postid) {
        return ResponseEntity.ok(debugApplicationService.getPostRaw(postid));
    }
}
