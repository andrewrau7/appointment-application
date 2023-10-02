package models;
/** Class for holding user information from the database. */
public abstract class  ActiveUser {

    private static int userId;
    private static String userName;



    /** Method for returning the current user's username.
        @return the current username.
     */
    public static String getUserName() {
        return userName;
    }


     /** Method for returning the active user ID number.
      Useful for checking any upcoming appointments within fifteen minutes for the active user.
      @return active user's id number.
      */
    public static int getUserId() {
        return userId;
    }



    /** Method for setting the current User's information.
     Information is held in ActiveUser class for checking upcoming appointment information and for
     updating the database's "Created_By" and "Update_By" columns.
     @param userId the ActiveUser's id from the database.
     @param userName the ActiveUser's username that matches the one stored in the database.
     */
    public static void login(int userId, String userName){
        ActiveUser.userId = userId;
        ActiveUser.userName = userName;
    }

}
