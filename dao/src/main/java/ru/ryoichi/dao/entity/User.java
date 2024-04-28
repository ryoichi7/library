package ru.ryoichi.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"books", "favourites"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Favourite> favourites = new HashSet<>();

    public void addBook(Book book) {
        if (!books.contains(book)){
            book.setUser(this);
            books.add(book);
        }
    }

    public void removeBook(Book book){
        if (books.contains(book)){
            books.remove(book);
            book.setUser(null);
        }
    }

    public void addFavourite(Favourite favourite) {
        if (!favourites.contains(favourite)){
            favourite.setUser(this);
            favourites.add(favourite);
        }
    }

    public void removeFavourite(Favourite favourite){
        if (favourites.contains(favourite)){
            favourites.remove(favourite);
            favourite.setUser(null);
            favourite.setBook(null);
        }
    }
}
