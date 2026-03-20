import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { listFiles, uploadFile, type ArquivoLoteria } from "../api/arquivos";
import { Alert, Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, Typography } from "@mui/material";
import UploadButton from "../components/UploadButton";
import { useMemo, useState } from "react";

function ArquivosPage() {
    const qc = useQueryClient();

    const q = useQuery({
        queryKey: ["arquivos"],
        queryFn: listFiles,
    });

    const mNew = useMutation({
        mutationFn: uploadFile,
        onSuccess: async () => {
            await qc.invalidateQueries({ queryKey: ["arquivos"] });
        },
    });

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const rows = q.data ?? [];

    const safePage = useMemo(() => {
        const maxPage = Math.max(0, Math.ceil(rows.length / rowsPerPage) - 1);
        return Math.min(page, maxPage);
    }, [page, rows.length, rowsPerPage]);

    const pagedRows = useMemo(() => {
        const start = safePage * rowsPerPage;
        return rows.slice(start, start + rowsPerPage);
    }, [rows, safePage, rowsPerPage]);

    return (
        <Box>
            <Paper
                sx={{
                    p: { xs: 2, sm: 2.5 },
                    mb: 2,
                    display: "flex",
                    alignItems: { xs: "stretch", sm: "center" },
                    flexDirection: { xs: "column", sm: "row" },
                    gap: 1.5,
                }}
                >
                <Box sx={{ flexGrow: 1 }}>
                    <Typography variant="h5" fontWeight={800}>Resultados</Typography>
                    <Typography variant="body2" color="text.secondary">
                        Faça upload do arquivo disponibilizado no site oficial
                    </Typography>
                </Box>
                <Box sx={{ alignSelf: { xs: "stretch", sm: "auto" }, ml: { sm: "auto" } }}>
                    <UploadButton onFile={(f) => mNew.mutate(f)} />
                </Box>                
            </Paper>

            {mNew.isError && <Alert severity="error">Falha ao fazer upload do arquivo.</Alert>}
            {mNew.isPending && <Alert severity="info">Uploado do arquivo em andamento...</Alert>}
            
            <Paper sx={{ overflow: "hidden" }}>
                <TableContainer sx={{ maxHeight: {xs: "60vh", sm: "70vh"} }}>
                    <Table stickyHeader size="small" aria-label="tabela de arquivos">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Nome</TableCell>
                                <TableCell>Data cadastro</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {pagedRows.map((a: ArquivoLoteria) => (
                                <TableRow key={a.id}>
                                    <TableCell sx={{ whiteSpace: "nowrap" }}>{a.id}</TableCell>
                                    <TableCell sx={{ minWidth: 220 }}>{a.name}</TableCell>
                                    <TableCell sx={{ whiteSpace: "nowrap" }}>{new Date(a.insertDate).toLocaleString()}</TableCell>
                                </TableRow>                                    
                            ))}
                            {q.isLoading && (
                                <TableRow>
                                    <TableCell colSpan={3}>Carregando...</TableCell>
                                </TableRow>
                            )}
                            {!q.isLoading && rows.length === 0 && (
                                <TableRow>
                                <TableCell colSpan={3}>Nenhum arquivo importado.</TableCell>
                                </TableRow>
                            )}                        
                        </TableBody>
                    </Table>
                    <TablePagination
                        component="div"
                        count={rows.length}
                        page={safePage}
                        onPageChange={(_, p) => setPage(p)}
                        rowsPerPage={rowsPerPage}
                        onRowsPerPageChange={(e) => {
                            setRowsPerPage(parseInt(e.target.value, 10));
                            setPage(0);
                        }}
                        rowsPerPageOptions={[5, 10, 15]}
                        labelRowsPerPage="Linhas por página" />
                </TableContainer>
            </Paper>
        </Box>
    )
}

export default ArquivosPage