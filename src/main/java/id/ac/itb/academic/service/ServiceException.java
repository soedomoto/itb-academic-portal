package id.ac.itb.academic.service;

public class ServiceException {
	
	public static class FacultyNotFoundException extends Exception {
		private static final long serialVersionUID = 8185862873818302455L;
		
		public FacultyNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class ProgramNotFoundException extends Exception {
		private static final long serialVersionUID = 5718387840270312786L;
		
		public ProgramNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class OptionNotFoundException extends Exception {
		private static final long serialVersionUID = -5897926728009613525L;
		
		public OptionNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class TeacherNotFoundException extends Exception {
		private static final long serialVersionUID = 8531093811966545201L;
		
		public TeacherNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class StudentNotFoundException extends Exception {
		private static final long serialVersionUID = 4185259205812404718L;
		
		public StudentNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class ClassNotFoundException extends Exception {
		private static final long serialVersionUID = -6565531992308023004L;
		
		public ClassNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class RoomNotFoundException extends Exception {
		private static final long serialVersionUID = 1687746758199423768L;
		
		public RoomNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class ScheduleNotFoundException extends Exception {
		private static final long serialVersionUID = -3285755551262060281L;
		
		public ScheduleNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class PresenceNotFoundException extends Exception {
		private static final long serialVersionUID = -4531319738723537065L;
		
		public PresenceNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class ResourceNotFoundException extends Exception {
		private static final long serialVersionUID = 2273351859762155927L;
		
		public ResourceNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class ImageNotFoundException extends Exception {
		private static final long serialVersionUID = 3272567095307151028L;

		public ImageNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class PdfNotFoundException extends Exception {
		private static final long serialVersionUID = 5046837353097783501L;

		public PdfNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class DocxNotFoundException extends Exception {
		private static final long serialVersionUID = 7815642041467869187L;

		public DocxNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class XlsxNotFoundException extends Exception {
		private static final long serialVersionUID = -4918532521601444167L;

		public XlsxNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class PptxNotFoundException extends Exception {
		private static final long serialVersionUID = -5383284379282646611L;

		public PptxNotFoundException(String msg) {
			super(msg);
		}
	}
	
	public static class StreamNotFoundException extends Exception {
		private static final long serialVersionUID = -1718924890683509620L;

		public StreamNotFoundException(String msg) {
			super(msg);
		}
	}
	
}
