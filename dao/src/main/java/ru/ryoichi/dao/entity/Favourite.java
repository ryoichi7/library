package ru.ryoichi.dao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(of = {"user", "book"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Favourite {
    @EmbeddedId
    private FavouriteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;
}
