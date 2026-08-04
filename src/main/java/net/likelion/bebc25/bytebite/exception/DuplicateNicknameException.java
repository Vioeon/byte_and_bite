package net.likelion.bebc25.bytebite.exception;

public class DuplicateNicknameException extends RuntimeException {
    /**
     * 예외 메시지를 전달받는 생성자
     *
     * @param message 예외 상세 메시지
     */
    public DuplicateNicknameException(String message) {
        super(message);
    }

    /**
     * 예외 메시지와 원인 예외(Cause)를 함께 전달받는 생성자
     *
     * @param message 예외 상세 메시지
     * @param cause 원인이 되는 상위 예외
     */
    public DuplicateNicknameException(String message, Throwable cause) {
        super(message, cause);
    }
}
