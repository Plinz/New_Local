import { ILocation } from 'app/shared/model//location.model';
import { IUser } from 'app/core/user/user.model';
import { IImage } from 'app/shared/model//image.model';

export interface IHolding {
    id?: number;
    siret?: string;
    name?: string;
    description?: string;
    location?: ILocation;
    owner?: IUser;
    images?: IImage[];
}

export class Holding implements IHolding {
    constructor(
        public id?: number,
        public siret?: string,
        public name?: string,
        public description?: string,
        public location?: ILocation,
        public owner?: IUser,
        public images?: IImage[]
    ) {}
}
