package ru.practicum.event.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.QEvent;
import ru.practicum.event.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AdminEventsFilters {
    private List<Long> users = new ArrayList<>();
    private List<State> states = new ArrayList<>();

    private List<Long> categories = new ArrayList<>();
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    public Predicate getPredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        if (users != null && !users.isEmpty()) {
            builder.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            builder.and(QEvent.event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }
        return builder.getValue();
    }
}
