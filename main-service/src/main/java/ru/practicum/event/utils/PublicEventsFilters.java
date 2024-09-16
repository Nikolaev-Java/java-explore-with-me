package ru.practicum.event.utils;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.querydsl.core.BooleanBuilder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.QEvent;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PublicEventsFilters {

    private List<Long> categories;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean paid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean onlyAvailable;
    @JsonSetter(nulls = Nulls.SKIP)
    private Sorted sort = Sorted.EVENT_DATE;
    @Size(min = 1, max = 7000)
    private String text;

    public enum Sorted {
        EVENT_DATE, VIEWS
    }

    public BooleanBuilder getPredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        if (categories != null && !categories.isEmpty()) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }
        if (onlyAvailable != null) {
            if (onlyAvailable) {
                builder.and(QEvent.event.participantLimit.ne(QEvent.event.confirmedRequests));
            }
        }
        if (rangeStart != null && rangeEnd != null) {
            builder.and(QEvent.event.eventDate.between(rangeStart, rangeEnd));
        } else if (rangeStart == null && rangeEnd == null) {
            builder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (text != null && !text.isEmpty()) {
            builder.and(QEvent.event.annotation.likeIgnoreCase(text)).or(QEvent.event.description.likeIgnoreCase(text));
        }
        return builder;
    }

    public Sort getSort() {
        if (sort.equals(Sorted.EVENT_DATE)) {
            return Sort.by(Sort.Direction.ASC, "eventDate");
        } else {
            return Sort.by(Sort.Direction.ASC, "views");
        }
    }
}