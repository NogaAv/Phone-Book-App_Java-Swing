package MidProject;

enum InputErrCode{ERR_EMPTY, INVALID_INPUT, INVALID_CHAR_INPUT, VALID_INPUT};

public class InputValidator {

    private static final int MIN_PHONE_DIGIT_COUNT = 8;

    public static InputErrCode validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            return InputErrCode.ERR_EMPTY;
        }
        if (phoneNumber.length() < MIN_PHONE_DIGIT_COUNT) {
            return InputErrCode.INVALID_INPUT;
        }

        String[] phoneNumbers = phoneNumber.split("-");
        String numbersOnly = "";
        for (int i = 0; i < phoneNumbers.length; ++i) {
            numbersOnly = numbersOnly.concat(phoneNumbers[i]);
        }
        for (int i = 0; i < numbersOnly.length(); ++i) {
            if (!Character.isDigit(numbersOnly.charAt(i))) {
                return InputErrCode.INVALID_CHAR_INPUT;
            }
        }
        return InputErrCode.VALID_INPUT;
    }

    public static InputErrCode validateEmail(String email){
        email = email.trim();
        if(email.isEmpty() || !email.contains("@")){
            return InputErrCode.INVALID_INPUT;
        }
        return InputErrCode.VALID_INPUT;
    }

    public static InputErrCode validateTextInput(String text){
        text = text.trim();
        if(text.isEmpty()){
            return InputErrCode.ERR_EMPTY;
        }

        return InputErrCode.VALID_INPUT;
    }
}
