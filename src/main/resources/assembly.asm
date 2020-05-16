and $8, $9, $11
xor $9, $10, $11
addu $8, $9, $1
slt $8, $9, $11
beq $8, $9, 0xfffffffd
bne $8, $9, 0xfffffffb 
ori $8, $1, 0x00000000 