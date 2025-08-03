package com.example.my_books_backend.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.my_books_backend.dto.favorite.FavoriteRequest;
import com.example.my_books_backend.dto.favorite.FavoriteResponse;
import com.example.my_books_backend.entity.User;
import com.example.my_books_backend.repository.UserRepository;
import com.example.my_books_backend.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "お気に入り")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private static final Long DEMO_USER_ID = 1L;
    
    /**
     * VERSION-1: デモ用固定ユーザー（ID=1）を取得
     */
    private User getDemoUser() {
        return userRepository.findById(DEMO_USER_ID)
            .orElseThrow(() -> new RuntimeException("Demo user not found"));
    }

    @Operation(description = "お気に入り追加")
    @PostMapping("")
    public ResponseEntity<FavoriteResponse> createFavorite(
        @Valid @RequestBody FavoriteRequest request
    ) {
        User user = getDemoUser();
        FavoriteResponse response = favoriteService.createFavorite(request, user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getId())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(description = "お気に入り削除")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(
        @PathVariable Long id
    ) {
        User user = getDemoUser();
        favoriteService.deleteFavorite(id, user);
        return ResponseEntity.noContent().build();
    }
}
