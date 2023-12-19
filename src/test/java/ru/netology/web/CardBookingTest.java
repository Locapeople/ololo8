package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.util.Objects.requireNonNull;

public class CardBookingTest {

    @Test
    void testCorrectInputs() {
        open("http://0.0.0.0:9999");
        SelenideElement form = $(".form.form_size_m.form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Уфа");
        // получить дату указанную в поле (сегодня)
        String currentDateString = form.$("[data-test-id=date] input").getValue();
        // сделать из нее объект даты с форматированием
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate currentDate = LocalDate.parse(requireNonNull(currentDateString), f);
        // прибавить 5 дней
        LocalDate newDate = currentDate.plusDays(5);
        String newDateString = f.format(newDate);
        // очистить пред. значение
        // я не знаю, как добраться до простого поля внутри calendar-input,
        // поэтому удалю старую дату, понажимав backspace 8 раз
        for(int i = 0; i < 8; i++) {
            form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        }
        // записать в поле новую дату
        form.$("[data-test-id=date] input").setValue(newDateString);
        form.$("[data-test-id=name] input").setValue("Фамилия Имя");
        form.$("[data-test-id=phone] input").setValue("+79998887766");
        form.$("[data-test-id=agreement]").click();
        form.$(byText("Забронировать")).click();

        $(withText("Встреча успешно забронирована")).shouldBe(visible, Duration.ofSeconds(15));
    }
}
