import { Moment } from 'moment';
import { IEmployee } from 'app/shared/model/employee.model';
import { IContractType } from 'app/shared/model/contract-type.model';
import { IProject } from 'app/shared/model/project.model';
import { ITechnology } from 'app/shared/model/technology.model';

export interface IResource {
  id?: number;
  startDate?: Moment;
  endDate?: Moment;
  note?: string;
  employee?: IEmployee;
  contractType?: IContractType;
  project?: IProject;
  technologies?: ITechnology[];
}

export class Resource implements IResource {
  constructor(
    public id?: number,
    public startDate?: Moment,
    public endDate?: Moment,
    public note?: string,
    public employee?: IEmployee,
    public contractType?: IContractType,
    public project?: IProject,
    public technologies?: ITechnology[]
  ) {}
}
