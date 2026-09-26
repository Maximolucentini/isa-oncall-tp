import { IProveedor } from 'app/entities/proveedor/proveedor.model';

export interface IContactoDeProveedor {
  id: number;
  nombre?: string | null;
  email?: string | null;
  telefono?: string | null;
  rol?: string | null;
  activo?: boolean | null;
  proveedor?: Pick<IProveedor, 'id' | 'nombre'> | null;
}

export type NewContactoDeProveedor = Omit<IContactoDeProveedor, 'id'> & { id: null };
