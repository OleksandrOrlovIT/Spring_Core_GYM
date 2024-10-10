package orlov.programming.springcoregym.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pageable {
    private int pageNumber;
    private int pageSize;
}