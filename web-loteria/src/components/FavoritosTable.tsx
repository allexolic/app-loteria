import { Button, Chip, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow } from "@mui/material";
import type { JogoFavorito } from "../api/favoritos";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useMemo, useState } from "react";

export default function FavoritosTable({data, onRecalcular, recalculandoId, }: { 
        data: JogoFavorito[];
        onRecalcular: (id: number) => void;
        recalculandoId?: number | null; }) {

            const [page, setPage] = useState(0);
            const [rowsPerPage, setRowsPerPage] = useState(10);

            const rows = data ?? [];

            const safePage = useMemo(() => {
                const maxPage = Math.max(0, Math.ceil(rows.length / rowsPerPage) - 1);
                return Math.min(page, maxPage);
            }, [page, rows.length, rowsPerPage]);

            const pagedRows = useMemo(() => {
                const start = safePage * rowsPerPage;
                return rows.slice(start, start + rowsPerPage);
            }, [rows, safePage, rowsPerPage]);

        return (
            <Paper>
                <TableContainer sx={{ width: "100%", overflowX: "auto" }}>
                    <Table stickyHeader size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>B1</TableCell>
                                <TableCell>B2</TableCell>
                                <TableCell>B3</TableCell>
                                <TableCell>B4</TableCell>
                                <TableCell>B5</TableCell>
                                <TableCell>B6</TableCell>
                                <TableCell>B7</TableCell>
                                <TableCell>B8</TableCell>
                                <TableCell>B9</TableCell>
                                <TableCell>B10</TableCell>
                                <TableCell>B11</TableCell>
                                <TableCell>B12</TableCell>
                                <TableCell>B13</TableCell>
                                <TableCell>B14</TableCell>
                                <TableCell>B15</TableCell>
                                <TableCell>Premiado</TableCell>
                                <TableCell>match</TableCell>
                                <TableCell align="center">Ações</TableCell>
                            </TableRow>    
                        </TableHead>
                        <TableBody>
                            {pagedRows.map((j) => (
                                <TableRow key={j.id}>
                                    <TableCell>{j.id}</TableCell>
                                    {j.balls.map((ball, index) => (
                                        <TableCell key={index}>{ball}</TableCell>
                                    ))}
                                    <TableCell>{j.awardWinningGame ? 
                                        <Chip label="SIM" color="success" variant="outlined" size="small"/> : 
                                        <Chip label="NÃO" color="error" variant="outlined" size="small"/>}</TableCell>
                                    <TableCell>{j.totalNumbersAwardsWinning}</TableCell>
                                    <TableCell align="right">
                                        <Button size="small" variant="outlined" startIcon={<RefreshIcon />} 
                                            onClick={() => onRecalcular(j.id)}
                                            disabled={recalculandoId === j.id}>
                                            Verificar
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
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
            </Paper>    
        )
 
}