import dayjs from 'dayjs/esm';

import { IContactoDeProveedor } from 'app/entities/contacto-de-proveedor/contacto-de-proveedor.model';
import { NivelGuardia } from 'app/entities/enumerations/nivel-guardia.model';

export interface ITurnoDeProveedor {
  id: number;
  desde?: dayjs.Dayjs | null;
  hasta?: dayjs.Dayjs | null;
  nivel?: keyof typeof NivelGuardia | null;
  esReemplazo?: boolean | null;
  nota?: string | null;
  contacto?: Pick<IContactoDeProveedor, 'id' | 'nombre'> | null;
}

export type NewTurnoDeProveedor = Omit<ITurnoDeProveedor, 'id'> & { id: null };
