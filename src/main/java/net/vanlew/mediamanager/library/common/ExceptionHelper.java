package net.vanlew.mediamanager.library.common;

public class ExceptionHelper {
    public static String getFlattenedMessageFriendly(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        boolean firstEx = true;
        String newExHeader = "\r\n";

        if (ex == null) {
            sb.append("Exception was empty/null.");
        } else {
            for (Throwable currEx = ex; currEx != null; currEx = currEx.getCause()) {
                if (!firstEx) {
                    sb.append(newExHeader);
                }
                sb.append(currEx.getMessage());
                firstEx = false;
            }
        }
        return sb.toString();
    }
}
