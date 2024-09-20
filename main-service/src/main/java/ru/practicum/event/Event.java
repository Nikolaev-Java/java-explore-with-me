package ru.practicum.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.category.Category;
import ru.practicum.comment.Comment;
import ru.practicum.participation.Participation;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(name = "category_user",
        attributeNodes = {@NamedAttributeNode(value = "initiator"), @NamedAttributeNode(value = "category")})
@NamedEntityGraph(name = "participation",
        attributeNodes = {@NamedAttributeNode(value = "participations"), @NamedAttributeNode("initiator")})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private User initiator;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @CreationTimestamp
    @Column(name = "creation_on")
    private LocalDateTime createdOn;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    @Builder.Default
    private boolean paid = false;
    @Column(columnDefinition = "INTEGER DEFAULT 0", name = "participant_limit")
    @Builder.Default
    private int participantLimit = 0;
    @Column(columnDefinition = "BOOLEAN DEFAULT true", name = "request_moderation")
    @Builder.Default
    private boolean requestModeration = true;
    private String title;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    @Builder.Default
    private int views = 0;
    @Column(columnDefinition = "INTEGER DEFAULT 0", name = "confirmed_requests")
    @Builder.Default
    private int confirmedRequests = 0;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    List<Participation> participations;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Comment> comments;
}
