package app.com.titus.communityapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@Builder
@ToString(exclude = "image")
public class ReportDto {
    private Long id;
    private String details;
    private String category;
    private double xCoordinate;
    private double yCoordinate;
    private String image;
    private boolean isActive;
    private String date;
}
