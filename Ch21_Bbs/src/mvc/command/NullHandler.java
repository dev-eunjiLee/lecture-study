package mvc.command;
// ??들어온 값이 없을때는 SC_NOT_Found 에러를 보내겠다!
import java.io.IOException;

// ?? 값이 안들어왔을 때 처리할 핸들러
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NullHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws IOException {
	// 인터페이스 CommandHandler에 throw가 없었는데 여기서 throw던지면  거기서도 자동으로 생성된다.
		
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		
		/*
		 * SC: status Code 상태코드(ex. 404, 500, ...)
		 */
		
		return null;
	}

}
