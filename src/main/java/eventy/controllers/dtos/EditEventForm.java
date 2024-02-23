package eventy.controllers.dtos;

import eventy.domain.Event;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EditEventForm {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Max. 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 200)
    private String description;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Must be a future date")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate date;

    @NotBlank(message = "Local is required")
    @Size(max = 50, message = "Max. 50 characters")
    private String local;

    private String bannerUrl;


    public Event toEntity() {
        Event newEvent = new Event();
        newEvent.setTitle(title);
        newEvent.setDescription(description);
        newEvent.setDate(date);
        newEvent.setLocal(local);
        newEvent.setBannerUrl(bannerUrl);

        return newEvent;
    }

    public static EditEventForm fromEntity(Event event) {
        EditEventForm form = new EditEventForm();
        form.id = event.getId();
        form.title = event.getTitle();
        form.description = event.getDescription();
        form.date = event.getDate();
        form.local = event.getLocal();
        form.bannerUrl = event.getBannerUrl();

        return form;
    }
}
