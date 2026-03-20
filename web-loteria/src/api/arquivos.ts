import { http } from "./http";

export type ArquivoLoteria = {
    id: number;
    name: string;
    insertDate: string;
};

export async function uploadFile(file: File) {
    const form = new FormData();
    form.append("file", file);
    const { data } = await http.post("/api/loteria/upload", form, {
        headers: { "Content-Type": "multipart/form-data"},
    });
    return data;
}

export async function listFiles(): Promise<ArquivoLoteria[]> {
    const { data } = await http.get("/api/loteria/arquivos");
    return data;
}