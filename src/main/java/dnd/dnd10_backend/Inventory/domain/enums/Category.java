package dnd.dnd10_backend.Inventory.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dnd.dnd10_backend.Inventory.domain.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    CIGARETTE("cigarette","담배"),
    GIFTCARD("giftcard","문화상품권"),
    GARBAGEBAG("garbagebag","쓰레기봉투");

    private final String value;
    private final String name;

    @JsonCreator
    public static Category from(String sub) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(sub)) {
                return category;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
