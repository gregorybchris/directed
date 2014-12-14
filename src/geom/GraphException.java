package geom;

public class GraphException extends RuntimeException {
	private static final long serialVersionUID = 0;
		public GraphException() {
			super();
		}
	  
		public GraphException(String message) {
			super(message);
		}
		
		public GraphException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public GraphException(Throwable cause) {
			super(cause);
		}
	}