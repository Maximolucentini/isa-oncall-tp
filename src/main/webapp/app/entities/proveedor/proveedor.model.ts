import { CoberturaProveedor } from 'app/entities/enumerations/cobertura-proveedor.model';
import { TipoProveedor } from 'app/entities/enumerations/tipo-proveedor.model';

export interface IProveedor {
  id: number;
  nombre?: string | null;
  tipo?: keyof typeof TipoProveedor | null;
  zonaHoraria?: string | null;
  telefonoContacto?: string | null;
  emailContacto?: string | null;
  urlSoporte?: string | null;
  urlEstado?: string | null;
  cobertura?: keyof typeof CoberturaProveedor | null;
  slaRespuestaMinutos?: number | null;
  activo?: boolean | null;
}

export type NewProveedor = Omit<IProveedor, 'id'> & { id: null };
