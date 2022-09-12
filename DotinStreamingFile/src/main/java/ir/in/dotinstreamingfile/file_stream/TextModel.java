package ir.in.dotinstreamingfile.file_stream;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TextModel {
    private String amount;
    private String trx;
    private String user;
}
