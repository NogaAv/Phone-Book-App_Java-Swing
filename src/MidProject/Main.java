package MidProject;

public class Main {

    public static void main(String[] args) {
        /**
         * Controller Contructor can be called with no parameters - and defaults to 'data.csv' file.
         * Parameters can be: String fileName  (- .csv or .json)
        * */
        Controller c = new Controller();
        c.appInit();
    }
}
