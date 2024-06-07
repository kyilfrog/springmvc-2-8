package hello.upload.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import hello.upload.domain.UploadFile;

@Component
public class FileStore {

	@Value("${file.dir}")
	private String fileDir;
	
	public String getFullPath(String filename) {
		return fileDir+filename;
	}
	
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if(!multipartFile.isEmpty()) {
				UploadFile uploadFile = storeFile(multipartFile);
				storeFileResult.add(uploadFile);
			}
		}
		return storeFileResult;
	}
	
	public UploadFile storeFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		if (multipartFile.isEmpty()) {
			return null;
		}
		
		String originalFilename = multipartFile.getOriginalFilename();
		
		String storeFileName = createStoreFileName(originalFilename);
		multipartFile.transferTo(new File(storeFileName));
		return new UploadFile(originalFilename, storeFileName);
	}
	
	private String createStoreFileName(String originalFilename) {
		String uuid = UUID.randomUUID().toString();  //uuid 생성
		String ext = extractExt(originalFilename);   //확장자 추출
		return uuid+"."+ext;
	}
	//확장자 추출하는 메서드
	private String extractExt(String originalFilename) {
		///문자열에서 특정 문자나 문자열이 마지막으로 등장하는 위치의 인덱스를 반환
		int pos = originalFilename.lastIndexOf(".");
		
		//지정한 인덱스부터 문자열의 끝까지의 부분 문자열을 반환
		return originalFilename.substring(pos+1);
	}
	
}

























