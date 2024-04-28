package ru.ryoichi.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = {"user", "title"})
@ToString(exclude = {"user", "favourites"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private String author;

    private String path;

    private Boolean visible;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_At")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Favourite> favourites = new HashSet<>();

    public void addUser(User user) {
        if (user != null) {
            user.getBooks().add(this);
            this.user = user;
        }
    }

    public void removeUser() {
        if (this.user != null) {
            this.user.getBooks().remove(this);
            this.user = null;
        }
    }

    public void addFavourite(Favourite favourite) {
        if (!favourites.contains(favourite)) {
            favourite.setBook(this);
            favourites.add(favourite);
        }
    }

    public void removeFavourite(Favourite favourite) {
        if (favourites.contains(favourite)) {
            favourites.remove(favourite);
            favourite.setBook(null);
            favourite.setUser(null);
        }
    }

}
