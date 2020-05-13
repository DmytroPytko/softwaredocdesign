import { IEmployee } from 'app/shared/model/employee.model';
import { IResource } from 'app/shared/model/resource.model';

export interface ITechnology {
  id?: number;
  name?: string;
  employyes?: IEmployee[];
  resources?: IResource[];
}

export class Technology implements ITechnology {
  constructor(public id?: number, public name?: string, public employyes?: IEmployee[], public resources?: IResource[]) {}
}
