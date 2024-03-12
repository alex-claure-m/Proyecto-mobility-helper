package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.values.DatosDeRegistro;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.usuarios.Rol;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsuariosController {
	@Getter
	private static final Repositorio<Usuario> repositorio;

	private static boolean adminCreado = false;

	static {
		repositorio = FactoryRepositorio.get(Usuario.class);
	}

	public static Boolean autenticar(String strUsuario, String strPassword) {
		Usuario usuario = buscarPorNombre(strUsuario);
		if(usuario != null && usuario.getPasswordUser().equals(strPassword))
			return Boolean.TRUE;

		return Boolean.FALSE;
	}

	public static Boolean autenticarRol(Usuario unUser, Integer rolId) {
		Set<Rol> rolesUser = unUser.getRoles();
		if(rolesUser.stream().anyMatch(unRol -> unRol.getId() == rolId))
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}


	public static Usuario buscarPorNombre(String strUsuario) {
		List<Usuario> usuarios = repositorio.buscarTodos();
		for(Usuario usuario : usuarios) {
			if(usuario.getNameUser().equals(strUsuario))
				return usuario;
		}

		return null;
	}
	// trato de que se busque por el nombre del rol y si existe ese nombre del rol
	// me devuelve el usuario para luego seguir comparando
	public static Usuario buscarRolPorNombre(String unRol) {
		List<Usuario> usuarios = repositorio.buscarTodos();
		for(Usuario usuario : usuarios) {
			if(usuario.getRoles().equals(unRol))
				return usuario;
		}

		return null;
	}

	public static Usuario buscarPorId(Integer idUsuario) {
		return repositorio.buscar(idUsuario);
	}

	public static Integer idDeNombre(String strUsuario) {
		Usuario usuario = buscarPorNombre(strUsuario);
		if(usuario != null)
			return usuario.getId();

		return null;
	}

	public static Usuario obtenerDeRequest(Request request) {
		Integer idDelUsuario = LoginController.sesionId(request);
		if(idDelUsuario != null)
			return buscarPorId(idDelUsuario);

		return null;
	}

	public static ModelAndView getVerUsuarios(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		model.put("usuariosRegistrados", repositorio.buscarTodos());
		return new ModelAndView(model, "usuarios/usuarios.hbs");
	}

	public static void crearUsuario(DatosDeRegistro datosDeRegistro) {
		crearRolesSiNoExiste("miembro");
		crearRolesSiNoExiste("administrador_plataforma");
		crearRolesSiNoExiste("representante_entidad");
		crearRolesSiNoExiste("representante_organizacion");

		if(!adminCreado){
			crearAdminHardcoded();
			adminCreado = true;
		}

		//creamos un nuevo usuario y lo persistimos
		Usuario usuario = new Usuario();
		usuario.setNameUser(datosDeRegistro.usuario);
		usuario.setPasswordUser(datosDeRegistro.password);
		repositorio.agregar(usuario);

		//también persistimos su parte de Miembro (cualquier usuario que se registra
		//por la página será un miembro, por más que aún no sea de ninguna comunidad).
		Miembro miembro = new Miembro();
		miembro.setNombre(datosDeRegistro.nombre);
		miembro.setApellido(datosDeRegistro.apellido);
		miembro.setCorreoElectronico(datosDeRegistro.email);
		miembro.setUsuarioAsociado(usuario);
		MiembrosController.getRepositorio().agregar(miembro);
/*
		//esto se supone que es para cuando creo el usuario con su rol , se cree la clase Rol y el tipo Miembro
		Rol rolMiembro = RolController.buscarRolPorNombre("miembro");
		if (rolMiembro == null) {
			// Si el rol "Miembro" no existe, créalo y obtén su ID
			rolMiembro = new Rol();
			rolMiembro.setTipoRol("Miembro");
			FactoryRepositorio.get(Rol.class).agregar(rolMiembro);
		}
		usuario.agregarRol(rolMiembro);

		repositorio.modificar(usuario);

		System.out.println("Buscando rol por nombre--0: " + rolMiembro.getTipoRol());

		usuario.agregarRol(rolMiembro);
		System.out.println("Buscando rol por nombre: " + rolMiembro.getTipoRol());
*/
		Rol rolMiembro = RolController.buscarRolPorNombre("miembro");
		usuario.agregarRol(rolMiembro);
		repositorio.modificar(usuario);
	}

	//creo un método nuevo por las dudas, este solo crea al usuario y miembro
	public static void crearUsuarioMiembro(DatosDeRegistro datosDeRegistro) {
		//creamos un nuevo usuario y lo persistimos
		Usuario usuario = new Usuario();
		usuario.setNameUser(datosDeRegistro.usuario);
		usuario.setPasswordUser(datosDeRegistro.password);
		usuario.setTipoRol(TipoRol.MIEMBRO);
		repositorio.agregar(usuario);

		//también persistimos su parte de Miembro (cualquier usuario que se registra
		//por la página será un miembro, por más que aún no sea de ninguna comunidad).
		Miembro miembro = new Miembro();
		miembro.setNombre(datosDeRegistro.nombre);
		miembro.setApellido(datosDeRegistro.apellido);
		miembro.setCorreoElectronico(datosDeRegistro.email);
		miembro.setUsuarioAsociado(usuario);
		MiembrosController.getRepositorio().agregar(miembro);
	}

	private static void crearRolesSiNoExiste(String nombreRol) {
		//esto se supone que es para cuando creo el usuario con su rol , se cree la clase Rol y el tipo Miembro
		Rol rol = RolController.buscarRolPorNombre(nombreRol);
		if (rol == null) {
			// Si el rol "Miembro" no existe, créalo y obtén su ID
			rol = new Rol();
			rol.setTipoRol(nombreRol);
			FactoryRepositorio.get(Rol.class).agregar(rol);
		}
	}
	public static void crearAdminHardcoded(){
		DatosDeRegistro unosDatos = new DatosDeRegistro();
		//creamos un nuevo usuario y lo persistimos
		Usuario usuario = new Usuario();
		usuario.setNameUser("admin2");
		usuario.setPasswordUser("admin2");
		repositorio.agregar(usuario);


		Miembro miembro = new Miembro();
		miembro.setNombre("administrador");
		miembro.setApellido("plataforma");
		MiembrosController.getRepositorio().agregar(miembro);

		//esto se supone que es para cuando creo el usuario con su rol , se cree la clase Rol y el tipo Miembro
		Rol rolAdmin = RolController.buscarRolPorNombre("administrador_plataforma");
		if (rolAdmin == null) {
			// Si el rol "Miembro" no existe, créalo y obtén su ID
			rolAdmin = new Rol();
			rolAdmin.setTipoRol("administrador_plataforma");
			FactoryRepositorio.get(Rol.class).agregar(rolAdmin);
		}
		usuario.agregarRol(rolAdmin);
		repositorio.modificar(usuario);

	}
}
