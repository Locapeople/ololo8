package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.util.Objects.requireNonNull;

public class CardBookingTest {

    @Test
    void testCorrectInputs() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Уфа");
        // получить дату указанную в поле (сегодня)
        String currentDateString = $("[data-test-id=date] input").getValue();
        // получить новую дату (прибавить 5 дней)
        String newDateString = dateAdjustDaysStringFormatted(currentDateString, "dd.MM.uuuu", 5);
        // очистить пред. значение
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        // записать в поле новую дату
        $("[data-test-id=date] input").setValue(newDateString);
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();

        $("[data-test-id=notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно!\nВстреча успешно забронирована на " + newDateString));
    }

    String dateAdjustDaysStringFormatted(String date, String format, long days) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        LocalDate currentDate = LocalDate.parse(requireNonNull(date), f);
        LocalDate newDate = currentDate.plusDays(days);
        return f.format(newDate);
    }
}
