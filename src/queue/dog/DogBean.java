package queue.dog;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DogBean {
    private DogAction dogAction;
    private String dogName;
}
