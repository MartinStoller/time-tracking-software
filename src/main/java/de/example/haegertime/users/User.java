package de.example.haegertime.users;

import lombok.Data;

@Data
abstract class User {  // "abstract" is a non-access modifier for classes or methods. For more info look at notes.txt
    //TODO: implement attributes and methods(getter/setter/equals will be handled by annotation).
    // CAREFUL: Dont set things to private, because then childclasses can´t access it - set to protected instead
}
