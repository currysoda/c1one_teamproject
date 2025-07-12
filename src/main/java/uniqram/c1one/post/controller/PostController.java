package uniqram.c1one.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uniqram.c1one.global.success.SuccessResponse;
import uniqram.c1one.post.dto.*;
import uniqram.c1one.post.exception.PostSuccessCode;
import uniqram.c1one.post.service.PostLikeService;
import uniqram.c1one.post.service.PostService;
import uniqram.c1one.security.adapter.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponse>> createPost(
            @RequestBody @Valid PostCreateRequest postCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        PostResponse postResponse = postService.createPost(userId, postCreateRequest);
        return ResponseEntity.status(PostSuccessCode.POST_CREATED.getStatus())
                .body(SuccessResponse.of(PostSuccessCode.POST_CREATED, postResponse));
    }

    @GetMapping("/home/following")
    public ResponseEntity<List<HomePostResponse>> getFollowingRecentPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request
    ) {
        Long userId = userDetails.getUserId();
        List<HomePostResponse> homePosts = postService.getFollowingRecentPosts(userId);
        log.info("/api/post/home/following 에서 GET 요청이 전송되었습니다. = {}", request.getMethod());
        return ResponseEntity.ok(homePosts);
    }

    @GetMapping("/home/recommend")
    public ResponseEntity<List<HomePostResponse>> getRecommendedPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<HomePostResponse> homePosts = postService.getRecommendedPosts(userId);
        return ResponseEntity.ok(homePosts);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Page<UserPostResponse>> getUserPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserPostResponse> posts = postService.getUserPosts(userId, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        Long userId = userDetails.getUserId();
        PostDetailResponse postDetailResponse = postService.getPostDetail(userId, postId);
        return ResponseEntity.ok(postDetailResponse);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        Long userId = userDetails.getUserId();
        PostResponse updatedPost = postService.updatePost(userId, postId, postUpdateRequest);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        Long userId = userDetails.getUserId();
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponse> likePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ){
        Long userId = userDetails.getUserId();
        PostLikeResponse like = postLikeService.like(userId, postId);
        return ResponseEntity.ok(like);
    }
}
