import { ILocation } from 'app/shared/model//location.model';
import { IPerson } from 'app/shared/model//person.model';

export interface IHolding {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
    location?: ILocation;
    person?: IPerson;
}

export class Holding implements IHolding {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public location?: ILocation,
        public person?: IPerson
    ) {}
}
