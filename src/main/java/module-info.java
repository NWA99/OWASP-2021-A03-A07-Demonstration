module com.darwishkolbwolf.infosec {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
	requires jbcrypt;
    requires transitive javafx.graphics;

    opens com.darwishkolbwolf.infosec to javafx.fxml;
    exports com.darwishkolbwolf.infosec;
}
