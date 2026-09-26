import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IServicioDeProveedor {
  id: number;
  nombre?: string | null;
  identificadorExterno?: string | null;
  descripcion?: string | null;
  slaRespuestaMinutos?: number | null;
  activo?: boolean | null;
  proveedor?: Pick<IProveedor, 'id' | 'nombre'> | null;
  servicioInternos?: Pick<IServicio, 'id' | 'nombre'>[] | null;
}

export type NewServicioDeProveedor = Omit<IServicioDeProveedor, 'id'> & { id: null };
