CREATE TABLE IF NOT EXISTS handphones (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(100) NOT NULL,
    merek VARCHAR(100) NOT NULL,
    harga BIGINT NOT NULL,
    deskripsi TEXT NOT NULL,
    spesifikasi TEXT NOT NULL,
    path_gambar VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
    );