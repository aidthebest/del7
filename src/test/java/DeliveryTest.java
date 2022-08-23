import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class DeliveryTest {
    private WebDriver driver;

    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    void shouldSendFormDelivery() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        String expected = "Встреча успешно забронирована на " + dayToDelivery;
        String actual = $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).getText().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFailWithCityNotHave() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Мамеев");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id='city'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldFailDeliveryLossThreeDays() {
        String dayToDelivery = date(2);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithSymbol() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Маме-ев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Встреча успешно забронирована на " + dayToDelivery));
    }

    @Test
    void shouldNotSendFormWithSymbol() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("М@меев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=name].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSendFormWithNotCorrectPhone() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=phone].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    @Test
    void shouldNotSendFormWithNotCorrectPhone2() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("+7999999999938726848");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=phone].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotSendFormWithoutFlag() {
        String dayToDelivery = date(7);
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (dayToDelivery));
        $("[data-test-id=name] input").setValue("Мамеев Петр");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $(By.className("button__text")).click();
        $("[data-test-id=agreement].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
