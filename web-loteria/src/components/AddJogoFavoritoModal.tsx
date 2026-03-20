import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Stack, TextField } from "@mui/material";
import React, { useEffect, useMemo, useRef, useState } from "react";

type Props = {
    open: boolean;
    onClose: () => void;
    onSubmit: (bolas: number[]) => void;
    loading: boolean;
}

function parseNumbers(text: string): number[] {
    return text
    .split(/[\s,;]+/)
    .map((x) => x.trim())
    .filter(Boolean)
    .map((x) => Number(x));
}

function generateRandomNumbers(): number[] {
    const newSet = new Set<number>();
    while (newSet.size < 15)
        newSet.add(1 + Math.floor(Math.random() * 25));
    return Array.from(newSet).sort((a,b) => a- b);
}

export default function AddJogoFavoritoModal({ open, onClose, onSubmit, loading }: Props) {
    const [vals, setVals] = useState<string[]>(Array.from({ length: 15 }, () => ""));
    const refs = useRef<Array<HTMLInputElement | null>>([]);

    useEffect(() => {
        if(open) {
            setVals(Array.from({ length: 15 }, () => ""));
            setTimeout(() => refs.current[0]?.focus(), 50);
        }
    }, [open]);

    const numbers = useMemo(() => {
        return vals.map((v) => (v.trim() === "" ? null : Number(v)));
    }, [vals]);

    const duplicates = useMemo(() => {
        const count = new Map<number, number>();
        numbers.forEach((n) => {
            if(n == null || !Number.isFinite(n))
                return;
            count.set(n, (count.get(n) ?? 0) + 1);
        });
        const duplicates = new Set<number>();
        for(const [k, c] of count.entries())
            if(c >1)
                duplicates.add(k);
        return duplicates;
    }, [numbers]);

    const hasDuplicates = duplicates.size > 0;

    const hasInvalid = useMemo(() => {
        return numbers.some((n) => {
            if(n == null)
                return true;
            if(!Number.isInteger(n))
                return true;
            return n < 1 || n > 25;
        });
    }, [numbers]);

    function setNumero(i: number, newVal: string) {
        const newValue = newVal.replace(/[^\d]/g, "").slice(0,2);
        setVals((prev) => {
            const next = [...prev];
            next[i] = newValue;
            return next;
        })
    }

    function fieldFocus(i: number) {
        refs.current[i]?.focus();
        refs.current[i]?.select();
    }

    function handleChange(i: number, v: string) {
        setNumero(i,v);
        const newValue = v.replace(/[^\d]/g, "").slice(0,2);
        if(newValue.length === 2 && i < 14) {
            setTimeout(() => fieldFocus(i + 1),0);
        }
    }

    function handleKeyDown(i: number, e: React.KeyboardEvent<HTMLDivElement>) {
        if(e.key === "Backspace" && vals[i] === "" && i > 0) {
            e.preventDefault();
            fieldFocus(i -1);
            return;
        }
        if((e.key === "Enter" || e.key === " " || e.key === "Tab") && i < 14) {
            if(e.key !== "Tab")
                e.preventDefault();
            fieldFocus(i);
        }
    }

    function handlePaste(i: number, e: React.ClipboardEvent<HTMLDivElement>) {
        const text = e.clipboardData.getData("text");
        const parsedNumbers = parseNumbers(text).filter((n) => Number.isFinite(n));
        if(parsedNumbers.length <=1)
            return;
        e.preventDefault();
        setVals((prev) => {
            const next = [...prev];
            let idx = i;
            for(const n of parsedNumbers) {
                if(idx > 14)
                    break;
                const formatedNumber = Math.trunc(n);
                next[idx] = String(formatedNumber);
                idx++;
            }
            return next;
        });
        const nextFocus = Math.min(14, i + parsedNumbers.length);
        setTimeout(() => fieldFocus(nextFocus), 0);
    }

    const canSubmit = !loading && !hasInvalid && !hasDuplicates;

    function submit() {
        const arrayNumbers = numbers.map((n) => n ?? 0);
        const formatedNumbers = arrayNumbers.map((n) => Math.trunc(n));
        formatedNumbers.sort((a,b) => a - b);
        onSubmit(formatedNumbers);
    }

    function fillRandom() {
        const r = generateRandomNumbers();
        setVals(r.map(String));
        setTimeout(() => fieldFocus(14), 0)
    }

    function clearAll() {
        setVals(Array.from({ length: 15 }, () => ""));
        setTimeout(() => fieldFocus(0), 0);
    }

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Novo jogo favorito</DialogTitle>
            <DialogContent>
                <Stack direction="row" spacing={1} sx={{ mt: 1, mb: 2 }} justifyContent="flex-end">
                    <Button size="small" variant="outlined" onClick={fillRandom}>Gerar aleatório</Button>
                    <Button size="small" variant="text" onClick={clearAll}>Limpar</Button>
                </Stack>

                <Box sx={{display: "grid", gridTemplateColumns: "repeat(5, minmax(0, 1fr))", gap: 1.2,}}>
                    {
                        vals.map((v, idx) => {
                            const n = numbers[idx];
                            const isEmpty = v.trim() === "";
                            const isRangeBad = !isEmpty && (!Number.isInteger(n) || (n as number) < 1 || (n as number) > 25); 
                            const isDuplicate = !isEmpty && n != null && duplicates.has(n);

                            return (                                
                                <TextField key={idx} label={`B${idx + 1}`} value={v} inputRef={(el) => (refs.current[idx] = el)}
                                    onChange={(e) => handleChange(idx, e.target.value)}
                                    onKeyDown={(e) => handleKeyDown(idx, e)}
                                    onPaste={(e) => handlePaste(idx, e)}
                                    error={isRangeBad || isDuplicate} 
                                    sx={{ mt: 1 }}/>
                            );
                        })
                    }                    
                </Box>
                {hasDuplicates && <Alert severity="warning" sx={{ mt: 2 }}>
                    Existem números repetidos. Cada jogo deve ter 15 números diferentes.</Alert>}

                {!hasDuplicates && hasInvalid && <Alert severity="info" sx={{ mt: 2 }}>
                    Preencha os 15 campos com números inteiros entre 1 e 25.</Alert>}                    
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancelar</Button>
                <Button variant="contained" disabled={!canSubmit} onClick={submit}>
                    Salvar
                </Button>
            </DialogActions>
        </Dialog>
    )    
}