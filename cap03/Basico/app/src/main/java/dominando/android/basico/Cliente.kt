package dominando.android.basico

import org.parceler.Parcel

@Parcel
class Cliente(var codigo: Int = 0, var nome: String = "")

/*
 * OUTRA IMPLEMENTACAO DA CLASSE QUE TERIA O MESMO EFEITO
 *
 * @Parcel
 * class Cliente(var codigo: Int, var nome: String) {
 *      @ParcelConstructor constructor: this(0, "")
 * }
 */