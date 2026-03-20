import { Button } from "@mui/material";
import { useRef } from "react"
import UploadFileIcon from "@mui/icons-material/UploadFile";

function UploadButton({ onFile }: {onFile: (f: File) => void }) {
    const ref = useRef<HTMLInputElement | null>(null);

    return (
        <>
            <input ref={ref} type="file" hidden accept=".xlsx" 
                onChange={(e) => {
                    const f = e.target.files?.[0];
                    if (f)
                        onFile(f);
                    if (ref.current)
                        ref.current.value = "";
                }}
            />
            <Button variant="contained" startIcon={<UploadFileIcon />} onClick={() => ref.current?.click()}>Upload</Button>
        </>
    )
}

export default UploadButton